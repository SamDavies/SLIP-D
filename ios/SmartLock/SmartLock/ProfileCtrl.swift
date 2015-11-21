//
//  ProfileCtrl.swift
//  SmartLock
//
//  Created by Sam Davies on 21/11/2015.
//  Copyright © 2015 Sam Davies. All rights reserved.
//

import UIKit

class ProfileCtrl: UIViewController {
    
    @IBAction func logout(sender: AnyObject) {
        LocksmithSmartLock.deleteUserPass()
    }
    
}
