///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.mariano.chatapp.chatappgui;
//
//import javafx.event.Event;
//import javafx.event.EventHandler;
//import javafx.scene.Parent;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyEvent;
//
///**
// *
// * @author Mariano
// */
//public class EnterKeyHandler implements EventHandler<KeyEvent> {
//
//    private KeyEvent keypress;
//
//    @Override
//    public void handle(KeyEvent event) {
//        if (keypress != null) {
//            keypress = null;
//            return;
//        }
//
//        Parent parent = myTextArea.getParent();
//        if (parent != null) {
//            if (event.getCode() == KeyCode.ENTER) {
//                if (event.isControlDown()) {
//                    keypress = recodeWithoutControlDown(event);
//                    myTextArea.fireEvent(keypress);
//                } else {
//                    Event parentEvent = event.copyFor(parent, parent);
//                    myTextArea.getParent().fireEvent(parentEvent);
//                }
//                event.consume();
//            }
//        }
//    }
//     
//
//    private KeyEvent recodeWithoutControlDown(KeyEvent event) {
//        return new KeyEvent(
//                event.getEventType(),
//                event.getCharacter(),
//                event.getText(),
//                event.getCode(),
//                event.isShiftDown(),
//                false,
//                event.isAltDown(),
//                event.isMetaDown()
//        );
//    }
//}

