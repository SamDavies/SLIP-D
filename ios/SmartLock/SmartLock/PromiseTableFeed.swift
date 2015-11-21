//
//  PromiseTableFeed.swift
//  SmartLock
//
//  Created by Sam Davies on 21/11/2015.
//  Copyright © 2015 Sam Davies. All rights reserved.
//

import UIKit

class PromiseTableFeed: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    //grid and scrollview referneces
    @IBOutlet var table : UITableView!
    
    override func viewDidLoad() {
        //DownloadBlocks(downloadURL)
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        self.table.dataSource = self
        self.table.delegate = self
        
        self.automaticallyAdjustsScrollViewInsets = false
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
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 0
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        return UITableViewCell()
    }
    
    func reloadCells(){
        dispatch_async(dispatch_get_main_queue(), {
            self.table!.reloadData()
        })
    }
}
