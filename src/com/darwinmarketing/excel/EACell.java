/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.darwinmarketing.excel;

/**
 *
 * @author jackie
 */
public class EACell {
    public int x = -1;
    public int y = -1;
    public String value = "";
    public EACell(int x,int y,String v){
        this.x  = x;
        this.y = y;
        this.value = v;
    }
}
