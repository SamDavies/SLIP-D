//
//  PromiseGridFeed.swift
//  SmartLock
//
//  Created by Sam Davies on 02/11/2015.
//  Copyright © 2015 Sam Davies. All rights reserved.
//

import UIKit

class PromiseGridFeed: UIViewController, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    
    //grid and scrollview referneces
    @IBOutlet var grid : UICollectionView!
    
    override func viewDidLoad() {
        //DownloadBlocks(downloadURL)
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        self.grid.dataSource = self
        self.grid.delegate = self
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        self.getGridObjects()
    }
    
    func getGridObjects(){
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func collectionView(collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 0
    }
    
    func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
        return UICollectionViewCell()
    }
    
    func reloadCells(){
        dispatch_async(dispatch_get_main_queue(), {
            self.grid!.reloadData()
        })
    }
}
