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
    @IBOutlet var openCloseButton: UIButton!
    @IBOutlet var lockName: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        openCloseButton.layer.cornerRadius = 40.0
        openCloseButton.layer.masksToBounds = true
        
        openCloseButton.layer.shadowColor = UIColor.blackColor().CGColor
        openCloseButton.layer.shadowOpacity = 0.8
        openCloseButton.layer.shadowRadius = 120
        openCloseButton.layer.shadowOffset = CGSizeMake(12.0, 12.0)
    }
    
    func create(lock: Lock) {
        self.lock = lock
        lockName.text = lock.name
        
        // set initial state
        updateButton()
        
        if(lock.requestedOpen && !lock.actuallyOpen){
            self.pollLockIsOpen(lock.id)
        }
        
        if(!lock.requestedOpen && lock.actuallyOpen){
            self.pollLockIsClosed(lock.id)
        }
        
//        User.getUserList(self.lock.id).then {
//            users -> Void in
//            
//        }
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
        return 0
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        return UITableViewCell()
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        
    }
    
    func pollLockIsOpen(id: Int) {
        Lock.getLock(id).then {
            lock -> Void in
            self.lock = lock
            self.updateButton()
            if(!(lock.requestedOpen && lock.actuallyOpen)){
                sleep(1)
                debugPrint("waiting to open")
                self.pollLockIsOpen(lock.id)
            }
        }
    }
    
    func pollLockIsClosed(id: Int) {
        Lock.getLock(id).then {
            lock -> Void in
            self.lock = lock
            self.updateButton()
            if(!(!lock.requestedOpen && !lock.actuallyOpen)){
                sleep(1)
                debugPrint("waiting to close")
                self.pollLockIsClosed(lock.id)
            }
        }
    }
    
    func setClosed() {
        dispatch_async(dispatch_get_main_queue(), {
            // swap the open state
            self.openCloseButton.setTitle("-", forState: UIControlState.Normal)
        })
    }
    
    func setWaiting() {
        dispatch_async(dispatch_get_main_queue(), {
            // swap the open state
            self.openCloseButton.setTitle("...", forState: UIControlState.Normal)
        })
    }
    
    func setOpen() {
        dispatch_async(dispatch_get_main_queue(), {
            // swap the open state
            self.openCloseButton.setTitle("O", forState: UIControlState.Normal)
        })
    }
}
