//
//  AddToLockCell.swift
//  SmartLock
//
//  Created by Sam Davies on 14/01/2016.
//  Copyright © 2016 Sam Davies. All rights reserved.
//

import UIKit

class AddToLockCell: UITableViewCell {
    
    @IBOutlet var name: UILabel!
    
    @IBOutlet var addFriendButton: UIButton!
    
    var lock: Lock!
    
    @IBAction func addFriend(sender: UIButton) {
        // TODO
        dispatch_async(dispatch_get_main_queue(), {
            self.addFriendButton.enabled = false
        })
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
    }
    
    func create(lock: Lock) {
        self.lock = lock
        name.text = lock.name
    }
}
