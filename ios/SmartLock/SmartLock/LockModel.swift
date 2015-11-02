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
    
    init(id: Int, isLocked: Bool) {
        self.id = id
        self.isLocked = isLocked
    }
    
    convenience init(json: JSON){
        let id = json["id"].int!
        let isLocked = json["is_locked"].bool!
        
        self.init(id: id, isLocked: isLocked)
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
}