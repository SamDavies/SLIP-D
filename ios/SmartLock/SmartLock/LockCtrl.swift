//
// Created by Sam Davies on 28/10/2015.
// Copyright (c) 2015 Sam Davies. All rights reserved.
//

import UIKit

class LockCtrl: PromiseGridFeed {
    
    var locks: [Lock] = []

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
        Lock.getLockList().then {
            (locks) -> Void in
            self.locks = locks
            self.reloadCells()
        }
        
    }
    
    override func collectionView(collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.locks.count
    }
    
    override func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
        let cell = grid.dequeueReusableCellWithReuseIdentifier("LockCell", forIndexPath: indexPath) as! LockCell
        cell.create(locks[indexPath.item])
        return cell
    }
    
    func collectionView(collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAtIndexPath indexPath: NSIndexPath) -> CGSize {
        
        // calculate the screen width
        var screenWidth: CGFloat = 320
        if(Settings.IS_IPHONE_6){
            screenWidth = 375
        }
        if(Settings.IS_IPHONE_6P){
            screenWidth = 414
        }
        
        return CGSizeMake(screenWidth, 651)
    }
    
    override func prefersStatusBarHidden() -> Bool {
        return true
    }

}
