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
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
    }
    
    func create(user: User) {
        name.text = user.firstName + " " + user.lastName
        email.text = user.email
    }
}
