//
// Created by Sam Davies on 28/10/2015.
// Copyright (c) 2015 Sam Davies. All rights reserved.
//

import UIKit

class LockCtrl: UIViewController {

    var isOpen = false

    @IBOutlet var openCloseButton: UIButton!

    override func viewDidLoad() {
        super.viewDidLoad()
    }

    @IBAction func openClose(sender: AnyObject) {
        // update the button on another thread
        dispatch_async(dispatch_get_main_queue(), {
            // swap the open state
            self.isOpen = !self.isOpen
            if(self.isOpen){
                self.openCloseButton.setTitle("Close Lock", forState: UIControlState.Normal)
            } else {
                self.openCloseButton.setTitle("Open Lock", forState: UIControlState.Normal)
            }
        })
    }
}
