/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.rest;


public enum Events {

    PARK_CALL,//(park the user ; only in established state)
    RESUME_CALL,//(Resume user from parkinglot case 1: admin channel cut->pick user)
    BRIDGE_CALL, //Bridge the call to another agent
    JOIN_CALL, //Put the caller into the conference(Put destination2conf+pick user+put agent and user to conference)
    HAND_OVER_CALL, //(Request for the first agent to end the call and hand it to the second agent, End agent's channel from conf)
    HANG_UP_CALL    //(Request to force the second agent hang up the call, case 1: talking to dest agent->cut the call; case 2:cut the admin from conf) 
}
