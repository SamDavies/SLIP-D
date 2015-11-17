//
//  LockTest.swift
//  SmartLock
//
//  Created by Sam Davies on 02/11/2015.
//  Copyright © 2015 Sam Davies. All rights reserved.
//

import UIKit
import XCTest
import Alamofire
import SwiftyJSON
import PromiseKit
@testable import SmartLock

class LockTest: XCTestCase {
    
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
    
    func testAddLock() {
        let versionBuild: String = NSBundle.mainBundle().infoDictionary!["CFBundleVersion"] as! String
        Lock.addLock(Int(versionBuild)!, name: "sam").then {
            lock -> Void in
            XCTAssertEqual(lock.requestedOpen, false)
            self.expectation.fulfill()
        }
        waitForExpectationsWithTimeout(5.0, handler: nil)
    }
    
    func testLockList() {
        Lock.getLockList().then {
            locks -> Void in
            XCTAssert(true)
            self.expectation.fulfill()
        }
        waitForExpectationsWithTimeout(5.0, handler: nil)
    }
    
    func testGetLock() {
        let versionBuild: String = NSBundle.mainBundle().infoDictionary!["CFBundleVersion"] as! String
        Lock.addLock(Int(versionBuild)! + 2000, name: "sam").then {
            lock -> Promise<Lock> in
            return Lock.getLock(lock.id)
        }.then {
            lock -> Void in
            XCTAssert(true)
            self.expectation.fulfill()
        }
        waitForExpectationsWithTimeout(5.0, handler: nil)
    }
    
    func testOpenLock() {
        let versionBuild: String = NSBundle.mainBundle().infoDictionary!["CFBundleVersion"] as! String
        
        Lock.addLock(Int(versionBuild)! + 1000, name: "sam").then {
            lock -> Promise<Lock> in
            return Lock.openLock(Int(versionBuild)! + 1000)
        }.then {
            lock -> Void in
            XCTAssertEqual(lock.requestedOpen, true)
            self.expectation.fulfill()
        }
        waitForExpectationsWithTimeout(5.0, handler: nil)
    }
}