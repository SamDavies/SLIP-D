//
//  UserModel.swift
//  SmartLock
//
//  Created by Sam Davies on 10/11/2015.
//  Copyright © 2015 Sam Davies. All rights reserved.
//

import Foundation
import SwiftyJSON
import PromiseKit

class User {
    var id: Int
    var email: String
    var firstName: String
    var lastName: String
    
    init(id: Int, email: String, firstName: String, lastName: String) {
        self.id = id
        self.email = email
        self.firstName = firstName
        self.lastName = lastName
    }
    
    convenience init(json: JSON){
        let id = json["id"].int!
        let email = json["email"].string!
        let firstName = json["first_name"].string!
        let lastName = json["last_name"].string!
        
        self.init(id: id, email: email, firstName: firstName, lastName: lastName)
    }
}

extension User {
    static func getUserList(lockId: Int?) -> Promise<[User]> {
        let session = TransportSession()
        session.url = "user"
        session.method = .GET
        session.returnsMultiJson = true
        if let lockId = lockId{
            session.parameters = ["lock_id": String(lockId)]
        }
        
        // return the promise with an array of objects
        return session.basicRequestPromise().then {
            (json: JSON) -> [User] in
            
            var users: [User] = []
            for (_, subJson): (String, JSON) in json {
                users.append(User(json: subJson))
            }
            return users
        }
    }
    
    static func getFriendList(search: String?) -> Promise<[User]> {
        let session = TransportSession()
        session.url = "friend"
        session.method = .GET
        session.returnsMultiJson = true
        if let search = search{
            session.parameters = ["search": search]
        }
        
        // return the promise with an array of objects
        return session.basicRequestPromise().then {
            (json: JSON) -> [User] in
            
            var users: [User] = []
            for (_, subJson): (String, JSON) in json {
                users.append(User(json: subJson))
            }
            return users
        }
    }
    
    static func getUser(userId: Int) -> Promise<User> {
        let session = TransportSession()
        session.url = "user/" + String(userId)
        session.method = .GET
        session.returnsMultiJson = true
        
        return session.basicRequestPromise().then {
            (json: JSON) -> User in
            return User(json: json)
        }
    }
    
    static func addUser(email: String, password: String, firstName: String, lastName: String) -> Promise<User> {
        let session = TransportSession()
        session.url = "user"
        session.method = .POST
        session.returnsMultiJson = true
        session.parameters = ["email": email, "password": password, "first_name": firstName, "last_name": lastName]
        
        // return the promise with an array of objects
        return session.basicRequestPromise().then {
            (json: JSON) -> User in
            return User(json: json)
        }
    }
}