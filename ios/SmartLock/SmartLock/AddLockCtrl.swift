//
//  AddLockCtrl.swift
//  SmartLock
//
//  Created by Sam Davies on 10/11/2015.
//  Copyright © 2015 Sam Davies. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON
import Locksmith
import PromiseKit

class AddLockCtrl: UIViewController, UITextFieldDelegate {
    
    // text fields
    @IBOutlet weak var idField: UITextField!
    @IBOutlet weak var nameField: UITextField!
    
    @IBOutlet weak var userPrompt: UILabel!
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    
    @IBOutlet var addLockButton: UIButton!

    override func viewDidLoad() {
        super.viewDidLoad()
        addLockButton.layer.cornerRadius = 8.0
    }
    
    @IBAction func onAddLock(sender: AnyObject) {
        self.dismissKeyboardAll()
        if(self.idField.text! != ""){
            self.addLock()
        }else{
            self.setPrompt("Please enter an id")
        }
    }
    
    func addLock(){
        Lock.addLock(Int(idField.text!)!, name: nameField.text!).then {
            lock -> Void in
            self.setPrompt("Success, lock added.")
            self.deactivateSpinner()
        }
    }
    
    func acitvateSpinner(){
        dispatch_async(dispatch_get_main_queue(), {
            self.activityIndicator.startAnimating()
        })
    }
    
    func deactivateSpinner(){
        dispatch_async(dispatch_get_main_queue(), {
            self.activityIndicator.stopAnimating()
        })
    }
    
    func setPrompt(toText: String){
        dispatch_async(dispatch_get_main_queue(), {
            self.userPrompt.text! = toText
        })
    }
    
    func clearPrompt(){
        // reset the prompt text
        self.setPrompt("")
    }
    
    func dismissKeyboardAll(){
        idField.resignFirstResponder()
        nameField.resignFirstResponder()
    }
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true;
    }
}