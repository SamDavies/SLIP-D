//
//  UserTest.swift
//  SmartLock
//
//  Created by Sam Davies on 10/11/2015.
//  Copyright © 2015 Sam Davies. All rights reserved.
//

import UIKit
import XCTest
import Alamofire
import SwiftyJSON
import PromiseKit
@testable import SmartLock

class UserTest: XCTestCase {
    
    var expectation: XCTestExpectation = XCTestExpectation()
    
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
        self.expectation = expectationWithDescription("Swift Expectations")
        
        LocksmithSmartLock.deleteUserPass()
        LocksmithSmartLock.saveUserPass("tester@mail.com", pass: "python")
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
        LocksmithSmartLock.deleteUserPass()
    }
    
    func testAddUser() {
        let versionBuild: String = NSBundle.mainBundle().infoDictionary!["CFBundleVersion"] as! String
        User.addUser(versionBuild + "@mail.com", password: "python", firstName: "test", lastName: "user").then {
            lock -> Void in
            XCTAssert(true)
            self.expectation.fulfill()
        }
        waitForExpectationsWithTimeout(5.0, handler: nil)
    }
    
    func testGetUserList() {
        User.getUserList(nil).then {
            users -> Void in
            XCTAssert(true)
            self.expectation.fulfill()
        }
        waitForExpectationsWithTimeout(5.0, handler: nil)
    }
    
    func testGetFriendList() {
        User.getFriendList(nil).then {
            users -> Void in
            XCTAssert(true)
            self.expectation.fulfill()
        }
        waitForExpectationsWithTimeout(5.0, handler: nil)
    }
    
//    func testGetUserListFilterByLock() {
//        let versionBuild: String = NSBundle.mainBundle().infoDictionary!["CFBundleVersion"] as! String
//        User.addUser(versionBuild + "@mail.com", password: "python", firstName: "test", lastName: "user").then {
//            user -> Promise<[User]> in
//            return User.getUserList(0)
//        }.then {
//            users -> Void in
//            XCTAssertEqual(users.count, 0)
//            self.expectation.fulfill()
//        }
//        waitForExpectationsWithTimeout(5.0, handler: nil)
//    }
    
    func testGetUser() {
        User.getUser(1).then {
            user -> Void in
            XCTAssert(true)
            self.expectation.fulfill()
        }
        waitForExpectationsWithTimeout(5.0, handler: nil)
    }
}
