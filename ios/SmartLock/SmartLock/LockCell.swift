//
//  LockCell.swift
//  SmartLock
//
//  Created by Sam Davies on 03/11/2015.
//  Copyright © 2015 Sam Davies. All rights reserved.
//

import UIKit

class LockCell: UICollectionViewCell, UITableViewDelegate, UITableViewDataSource {
    
    var lock: Lock!
    var users: [User] = []
    @IBOutlet var openCloseButton: UIButton!
    @IBOutlet var lockName: UILabel!
    @IBOutlet var table : UITableView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        openCloseButton.layer.cornerRadius = 45.0
        openCloseButton.layer.masksToBounds = true
        
        openCloseButton.layer.shadowColor = UIColor.blackColor().CGColor
        openCloseButton.layer.shadowOpacity = 0.8
        openCloseButton.layer.shadowRadius = 120
        openCloseButton.layer.shadowOffset = CGSizeMake(12.0, 12.0)
        
        // set initial state
        if lock != nil {
            figureOutButton()
        }
        
        self.table.dataSource = self
        self.table.delegate = self
        self.table.allowsSelection = false
    }
    
    func create(lock: Lock) {
        self.lock = lock
        lockName.text = lock.name
        
        // set initial state
        figureOutButton()
        
//        User.getUserList(self.lock.id).then {
//            users -> Void in
//            
//        }
        
        User.getUserList(lock.id).then {
            (users) -> Void in
            self.users = users
            self.reloadCells()
        }
    }
    
    func figureOutButton(){
        updateButton()
        
        if(lock.requestedOpen && !lock.actuallyOpen){
            self.pollLockIsOpen(lock.id)
        }
        
        if(!lock.requestedOpen && lock.actuallyOpen){
            self.pollLockIsClosed(lock.id)
        }
    }
    
    func updateButton(){
        if(lock.requestedOpen && lock.actuallyOpen){
            setOpen()
        }else {
            if(!lock.requestedOpen && !lock.actuallyOpen){
                setClosed()
            }else{
                setWaiting()
            }
        }
    }
    
    @IBAction func openClose(sender: AnyObject) {
        // update the button on another thread
        if(lock.requestedOpen && lock.actuallyOpen){
            Lock.closeLock(lock.id).then {
                lock -> Void in
                self.lock = lock
                self.updateButton()
                self.pollLockIsClosed(lock.id)
            }
        }else{
            if(!lock.requestedOpen && !lock.actuallyOpen){
                Lock.openLock(lock.id).then {
                    lock -> Void in
                    self.lock = lock
                    self.updateButton()
                    self.pollLockIsOpen(lock.id)
                }
            }
        }
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return users.count
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = table.dequeueReusableCellWithIdentifier("MemberCell", forIndexPath: indexPath) as! MemberCell
        cell.create(users[indexPath.item])
        return cell
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        
    }
    
    func reloadCells(){
        dispatch_async(dispatch_get_main_queue(), {
            self.table!.reloadData()
        })
    }
    
    func pollLockIsOpen(id: Int) {
        Lock.getLock(id).then {
            lock -> Void in
            self.lock = lock
            self.updateButton()
            if(!(lock.requestedOpen && lock.actuallyOpen)){
                let time = dispatch_time(dispatch_time_t(DISPATCH_TIME_NOW), 1 * Int64(NSEC_PER_SEC))
                dispatch_after(time, dispatch_get_main_queue()) {
                    //put your code which should be executed with a delay here
                    debugPrint("waiting to open")
//                    self.pollLockIsOpen(lock.id)
                }
            }
        }
    }
    
    func pollLockIsClosed(id: Int) {
        Lock.getLock(id).then {
            lock -> Void in
            self.lock = lock
            self.updateButton()
            if(!(!lock.requestedOpen && !lock.actuallyOpen)){
                let time = dispatch_time(dispatch_time_t(DISPATCH_TIME_NOW), 1 * Int64(NSEC_PER_SEC))
                dispatch_after(time, dispatch_get_main_queue()) {
                    //put your code which should be executed with a delay here
                    debugPrint("waiting to close")
//                    self.pollLockIsClosed(lock.id)
                }
            }
        }
    }
    
    func setClosed() {
        dispatch_async(dispatch_get_main_queue(), {
            // swap the open state
            self.openCloseButton.setImage(UIImage(named: "closed.png")!.imageWithRenderingMode(UIImageRenderingMode.AlwaysTemplate), forState: UIControlState.Normal)
        })
    }
    
    func setWaiting() {
        dispatch_async(dispatch_get_main_queue(), {
            // swap the open state
            self.openCloseButton.setImage(UIImage(named: "loading.png")!.imageWithRenderingMode(UIImageRenderingMode.AlwaysTemplate), forState: UIControlState.Normal)
        })
    }
    
    func setOpen() {
        dispatch_async(dispatch_get_main_queue(), {
            // swap the open state
            
            self.openCloseButton.setImage(UIImage(named: "open.png")!.imageWithRenderingMode(UIImageRenderingMode.AlwaysTemplate), forState: UIControlState.Normal)
        })
    }
}
