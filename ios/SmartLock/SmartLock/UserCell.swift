//
//  UserCell.swift
//  SmartLock
//
//  Created by Sam Davies on 21/11/2015.
//  Copyright © 2015 Sam Davies. All rights reserved.
//

import UIKit

class UserCell: UITableViewCell {
    
    @IBOutlet var name: UILabel!
    @IBOutlet var email: UILabel!
    
    @IBOutlet var addFriendButton: UIButton!
    
    var user: User!
    
    @IBAction func addFriend(sender: UIButton) {
        User.addFriend(user.id).then {
            user -> Void in
            debugPrint("adding friend")
            dispatch_async(dispatch_get_main_queue(), {
                self.addFriendButton.enabled = false
            })
        }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
    }
    
    func create(user: User) {
        self.user = user
        name.text = user.firstName + " " + user.lastName
        email.text = user.email
    }
}
