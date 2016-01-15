//
//  AddToLockCtrl.swift
//  SmartLock
//
//  Created by Sam Davies on 14/01/2016.
//  Copyright © 2016 Sam Davies. All rights reserved.
//

import UIKit

class AddToLockCtrl: PromiseTableFeed {
    
    var locks: [Lock] = []
    var user: User!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController!.navigationBar.barStyle = UIBarStyle.Black
        self.navigationController!.navigationBar.translucent = false
        self.automaticallyAdjustsScrollViewInsets = false
        self.table.allowsSelection = false
        
        self.navigationItem.title = user.firstName + " " + user.lastName
    }
    
    /*
    Get the post feed for the specified thing
    */
    override func getGridObjects() {
        //        self.spinner.startAnimating()
        Lock.getLockList().then {
            (locks) -> Void in
            self.locks = locks
            self.reloadCells()
        }
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.locks.count
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = table.dequeueReusableCellWithIdentifier("AddToLockCell", forIndexPath: indexPath) as! AddToLockCell
        cell.create(self.locks[indexPath.item])
        return cell
    }
}
