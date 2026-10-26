//
//  IOSSocketBridgeImpl.swift
//  iosApp
//
//  Created by Japp Tech on 17/08/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import SocketIO
import ComposeApp

final class IOSSocketBridgeImpl: IosChatBridge {

    private var manager: SocketManager?
    private var socket: SocketIOClient?
     private var listeners: [String: [IosChatListener]] = [:]


       override func connect(url: String) {
           manager = SocketManager(socketURL: URL(string: url)!, config: [.log(true), .compress])
                  let s = manager!.defaultSocket
                  socket = s

                  // lifecycle
                  s.on(clientEvent: .connect) { [weak self] _, _ in
                      guard let self else { return }
                      
                      self.fire(event: "connected", payload: nil) { l in l.onConnected() }
                  }
                  s.on(clientEvent: .disconnect) { [weak self] _, _ in
                      guard let self else { return }
                      
                      self.fire(event: "disconnected", payload: nil) { l in l.onDisconnected() }
                  }
                  s.on(clientEvent: .error) { [weak self] data, _ in
                      guard let self else { return }
                      let msg = data.first.map { String(describing: $0) } ?? "Socket error"
                      
                      self.fire(event: "error", payload: msg) { l in l.onError(msg: msg) }
                  }
                       s.on("message") { [weak self] data, _ in
                           guard let self else { return }
                           let raw: String
                           if let obj = data.first {
                               if let json = try? JSONSerialization.data(withJSONObject: obj),
                                  let str = String(data: json, encoding: .utf8) {
                                   raw = str
                               } else {
                                   raw = String(describing: obj)
                               }
                           } else {
                               raw = "{}"
                           }
                           self.fire(event: "message", payload: raw) { l in l.onMessage(message: raw) }
                           
                       }
           
           s.on("message-support") { [weak self] data, _ in
               guard let self else { return }
               let raw: String
               if let obj = data.first {
                   if let json = try? JSONSerialization.data(withJSONObject: obj),
                      let str = String(data: json, encoding: .utf8) {
                       raw = str
                   } else {
                       raw = String(describing: obj)
                   }
               } else {
                   raw = "{}"
               }
               self.fire(event: "message-support", payload: raw) { l in l.onMessage(message: raw) }
                          }
                   

                  // data events will be attached on demand in `on(event:listener:)`
                  s.connect()
       }

       override func disconnect() {
          socket?.disconnect()
                 socket = nil
                 manager = nil
                 listeners.removeAll()
       }

       override func emit(event: String, payloadJson: String) {
        
           if let data = try? JSONSerialization.jsonObject(with: Data(payloadJson.utf8)) {
               socket?.emit(event, data as! SocketData)
                  } else {
                      socket?.emit(event, payloadJson)
                  }
       }

    override func on(event: String, listener: IosChatListener) {
        if listeners[event] == nil {
            listeners[event] = []
        }
    
            listeners[event]?.append(listener)
        }
    
           private func fire(event: String, payload: String?, call: (IosChatListener) -> Void) {
              
               listeners[event]?.forEach { call($0) }
           }
   }
