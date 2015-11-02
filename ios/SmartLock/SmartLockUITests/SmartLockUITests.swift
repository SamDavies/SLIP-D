//
//  SmartLockUITests.swift
//  SmartLockUITests
//
//  Created by Sam Davies on 30/09/2015.
//  Copyright © 2015 Sam Davies. All rights reserved.
//

import XCTest

class SmartLockUITests: XCTestCase {
    
        
    override func setUp() {
        super.setUp()
        
        // Put setup code here. This method is called before the invocation of each test method in the class.
        
        // In UI tests it is usually best to stop immediately when a failure occurs.
        continueAfterFailure = false
        // UI tests must launch the application that they test. Doing this in setup will make sure it happens for each test method.
        XCUIApplication().launch()

        // In UI tests it’s important to set the initial state - such as interface orientation - required for your tests before they run. The setUp method is a good place to do this.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    func testExample() {
        let app = XCUIApplication()
        
        // log in the user
        let emailTextField = app.textFields["Email"]
        emailTextField.tap()
        emailTextField.typeText("tester@mail.com")
        let passwordSecureTextField = app.secureTextFields["Password"]
        passwordSecureTextField.tap()
        passwordSecureTextField.typeText("python")
        app.buttons["Login"].tap()
        
        // record the number of tabs
        let tabBarsQuery = app.tabBars
        let tabBarCount = tabBarsQuery.buttons.count
        
        // log the user out
        app.tabBars.buttons["Second"].tap()
        app.buttons["Log Out"].tap()
        
        // check the number of tabs
        XCTAssertEqual(tabBarCount, 2)
    }
    
}
