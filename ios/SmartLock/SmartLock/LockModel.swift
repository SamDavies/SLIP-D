//
//  Lock.swift
//  SmartLock
//
//  Created by Sam Davies on 01/11/2015.
//  Copyright © 2015 Sam Davies. All rights reserved.
//

import Foundation
import SwiftyJSON
import PromiseKit

class Lock {
    var id: Int
    var isLocked: Bool
    var name: String
    
    init(id: Int, isLocked: Bool, name: String) {
        self.id = id
        self.isLocked = isLocked
        self.name = name
    }
    
    convenience init(json: JSON){
        let id = json["id"].int!
        let isLocked = json["locked"].boolValue
        let name = json["name"].string!
        
        self.init(id: id, isLocked: isLocked, name: name)
    }
}

extension Lock {
    /// Fetch a list of locks that this user has access to from the API
    static func getLockList() -> Promise<[Lock]> {
        let session = TransportSession()
        session.url = "lock"
        session.method = .GET
        session.returnsMultiJson = true
        
        // return the promise with an array of objects
        return session.basicRequestPromise().then {
            (json: JSON) -> [Lock] in
            
            var locks: [Lock] = []
            for (_, subJson): (String, JSON) in json {
                locks.append(Lock(json: subJson))
            }
            return locks
        }
    }
    
    static func addLock(lockId: Int, name: String) -> Promise<Lock> {
        let session = TransportSession()
        session.url = "lock"
        session.method = .POST
        session.returnsMultiJson = true
        session.parameters = ["lock_id": String(lockId), "lock_name": name]
        
        // return the promise with an array of objects
        return session.basicRequestPromise().then {
            (json: JSON) -> Lock in
            return Lock(json: json)
        }
    }
    
//    static func openLock(lockId: Int) -> Promise<Lock> {
//        let session = TransportSession()
//        session.url = "open/" + String(lockId)
//        session.method = .PUT
//        session.returnsMultiJson = true
//        
//        // return the promise with an array of objects
//        return session.basicRequestPromise().then {
//            (json: JSON) -> Lock in
//            return Lock(json: json)
//        }
//    }
}