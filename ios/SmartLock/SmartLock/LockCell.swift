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
        openCloseButton.layer.cornerRadius = 150.0
    }
    
    @IBAction func openClose(sender: AnyObject) {
        // update the button on another thread
        dispatch_async(dispatch_get_main_queue(), {
            // swap the open state
            self.lock.isLocked = !self.lock.isLocked
            if(!self.lock.isLocked){
                self.openCloseButton.setTitle("CLOSE", forState: UIControlState.Normal)
            } else {
                self.openCloseButton.setTitle("OPEN", forState: UIControlState.Normal)
            }
        })
    }
    
    func create(lock: Lock) {
        self.lock = lock
        lockName.text = lock.name
    }
}
