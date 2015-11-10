//
//  User.swift
//  SmartLock
//
//  Created by Sam Davies on 02/11/2015.
//  Copyright © 2015 Sam Davies. All rights reserved.
//

import UIKit

class UserCtrl: UIViewController {
    
    @IBAction func logout(sender: AnyObject) {
        LocksmithSmartLock.deleteUserPass()
    }

}
