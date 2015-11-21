//
//  FriendCtrl.swift
//  SmartLock
//
//  Created by Sam Davies on 21/11/2015.
//  Copyright © 2015 Sam Davies. All rights reserved.
//

import UIKit

class FriendCtrl: PromiseTableFeed {
    
    var friends: [User] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController!.navigationBar.barStyle = UIBarStyle.Black
        self.navigationController!.navigationBar.translucent = false
        self.automaticallyAdjustsScrollViewInsets = false
    }
    
    /*
    Get the post feed for the specified thing
    */
    override func getGridObjects() {
        //        self.spinner.startAnimating()
        User.getFriendList(nil).then {
            (friends) -> Void in
            self.friends = friends
            self.reloadCells()
        }
        
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.friends.count
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = table.dequeueReusableCellWithIdentifier("FriendCell", forIndexPath: indexPath) as! FriendCell
        cell.create(friends[indexPath.item])
        return cell
    }
}