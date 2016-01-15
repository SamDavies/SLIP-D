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
    
    var selectedFriend: User!
    
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
    
    @IBAction func selectThing(sender: UIButton){
        let cell = sender.superview!.superview as! FriendCell
        selectedFriend = cell.user
        self.performSegueWithIdentifier("openLocks", sender: self)
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if(segue.identifier == "openLocks") {
            // pass the selected thing ID to Post Feed
            let lockCtrl = (segue.destinationViewController as! AddToLockCtrl)
            lockCtrl.user = self.selectedFriend
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
    
    func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        return true
    }
    
    func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
        if (editingStyle == UITableViewCellEditingStyle.Delete) {
            // handle delete (by removing the data from your array and updating the tableview)
            let friendId: Int = friends[indexPath.item].id
            friends.removeAtIndex(indexPath.item)
            
            User.deleteFriend(friendId).then  {
                user -> Void in
                
            }
            
            self.reloadCells()
        }
    }
}