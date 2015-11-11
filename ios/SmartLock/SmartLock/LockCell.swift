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
    
    @IBAction func openClose(sender: AnyObject) {
        // update the button on another thread
        dispatch_async(dispatch_get_main_queue(), {
            // swap the open state
            self.lock.isLocked = !self.lock.isLocked
            if(!self.lock.isLocked){
                self.openCloseButton.setTitle("-", forState: UIControlState.Normal)
            } else {
                self.openCloseButton.setTitle("O", forState: UIControlState.Normal)
            }
        })
    }
    
    func create(lock: Lock) {
        self.lock = lock
        lockName.text = lock.name
    }
}
