//
//  LockCell.swift
//  SmartLock
//
//  Created by Sam Davies on 03/11/2015.
//  Copyright © 2015 Sam Davies. All rights reserved.
//

import UIKit

class LockCell: UICollectionViewCell {
    
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
    
    @IBAction func open(sender: AnyObject) {
        // update the button on another thread
        Lock.openLock(lock.id).then {
            lock -> Void in
            self.setWaiting()
            self.pollLockIsOpen(lock.id)
        }
    }
    
    func pollLockIsOpen(id: Int) {
        Lock.getLock(id).then {
            lock -> Void in
            if(lock.actuallyOpen == false){
                sleep(1)
                self.pollLockIsOpen(lock.id)
            }else{
                debugPrint("lock opened")
                self.setOpen()
                self.pollLockIsClosed(lock.id)
            }
        }
    }
    
    func pollLockIsClosed(id: Int) {
        Lock.getLock(id).then {
            lock -> Void in
            if(lock.actuallyOpen == true){
                sleep(1)
                debugPrint("waiting to close")
                self.pollLockIsClosed(lock.id)
            }else{
                debugPrint("closed")
                self.setClosed()
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
            self.lock.requestedOpen = true
            self.openCloseButton.setTitle("...", forState: UIControlState.Normal)
        })
    }
    
    func setOpen() {
        dispatch_async(dispatch_get_main_queue(), {
            // swap the open state
            self.lock.requestedOpen = false
            self.openCloseButton.setTitle("O", forState: UIControlState.Normal)
        })
    }
    
    func create(lock: Lock) {
        self.lock = lock
        lockName.text = lock.name
        
        setClosed()
    }
}
