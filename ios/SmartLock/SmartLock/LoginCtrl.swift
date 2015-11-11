//
// Created by Sam Davies on 20/10/2015.
// Copyright (c) 2015 Sam Davies. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON
import Locksmith
import PromiseKit

class LoginCtrl: UIViewController, UITextFieldDelegate {

    // text fields
    @IBOutlet weak var emailField: UITextField!
    @IBOutlet weak var passwordField: UITextField!
    @IBOutlet weak var rePasswordField: UITextField!
    @IBOutlet weak var firstNameField: UITextField!
    @IBOutlet weak var lastNameField: UITextField!

    @IBOutlet var regView: UIView!

    // constraints
    @IBOutlet weak var loginBottomSpace: NSLayoutConstraint!
    @IBOutlet weak var regCenterSpace: NSLayoutConstraint!
    @IBOutlet weak var buttonsTopSpace: NSLayoutConstraint!
    @IBOutlet weak var loginToView: NSLayoutConstraint!

    @IBOutlet weak var userPrompt: UILabel!
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    var isViewRegister : Bool = false

    override func viewDidLoad() {
        super.viewDidLoad()

        // try to login if the credentials exist
        let (user, pass, exists) = LocksmithSmartLock.getUserPass()
        if(exists){
            emailField.text! = user
            passwordField.text! = pass
            login()
        }
    }

    @IBAction func unwindFromlogout(unwindSegue: UIStoryboardSegue) {
        LocksmithSmartLock.deleteUserPass()
        if let _ = unwindSegue.sourceViewController as? UserCtrl {
            print("Coming from UserCtrl")
        }
    }

    @IBAction func onLogin(sender: AnyObject) {
        if(isViewRegister){
            self.swapLayout(false)
        }else{
            self.dismissKeyboardAll()
            if(self.emailField.text! != ""){
                self.login()
            }else{
                self.setPrompt("Please enter an email")
            }
        }
    }

    @IBAction func onRegister(sender: AnyObject) {
        self.clearPrompt()

        if(!isViewRegister){
            self.swapLayout(true)
        }else{
            self.dismissKeyboardAll()
            
            if(self.emailField.text! != ""){
                acitvateSpinner()

                User.addUser(emailField.text!, password: passwordField.text!, firstName: firstNameField.text!, lastName: lastNameField.text!).then {
                    lock -> Void in
                    self.login()
                }
            }else{
                self.setPrompt("Please enter an email")
            }
        }
    }

    func login(){
        self.clearPrompt()
        acitvateSpinner()


        // first test the connection
        let session = TransportSession()
        session.url = "hello"
        session.method = Alamofire.Method.GET
        session.returnsMultiJson = true
        self.setPrompt("Testing Connection.")

        session.basicRequestPromise().then {
            (json: JSON) -> Promise<JSON> in
            self.setPrompt("connection good")
            LocksmithSmartLock.deleteUserPass()
            LocksmithSmartLock.saveUserPass(self.emailField.text!, pass: self.passwordField.text!)

            // Next try access a protected resource
            let session2 = TransportSession()
            session2.url = "protected-resource"
            session2.method = Alamofire.Method.GET
            session2.returnsMultiJson = true
            return session2.basicRequestPromise()
        }.then {
            (json: JSON) -> Void in
            self.setPrompt("Success.")
            self.performSegueWithIdentifier("showHome", sender: self)
        }.always {
            self.deactivateSpinner()
        }.error {
            error -> Void in
            // delete the remembered credetials so that another login is not attempted
            LocksmithSmartLock.deleteUserPass()
            self.setPrompt("Error returned \(error)")
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

    func swapLayout(displayRegister: Bool){
        if(displayRegister){
            self.regView.alpha = 1
            self.loginToView.constant = 100
            self.loginBottomSpace.constant = 8
            self.regCenterSpace.constant = -5
            self.buttonsTopSpace.constant = 8
        }else{
            self.regView.alpha = 0
            self.loginToView.constant = 155
            self.loginBottomSpace.constant = -89
            self.regCenterSpace.constant = self.view.frame.width/2 + 203/2
            self.buttonsTopSpace.constant = -22
        }

        UIView.animateWithDuration(0.2, animations: { () in
            self.view.layoutIfNeeded()
        })

        // allow a login to be sent
        isViewRegister = displayRegister
    }

    func dismissKeyboardAll(){
        emailField.resignFirstResponder()
        passwordField.resignFirstResponder()
        rePasswordField.resignFirstResponder()
        firstNameField.resignFirstResponder()
        lastNameField.resignFirstResponder()
    }

    func textFieldShouldReturn(textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true;
    }
}
