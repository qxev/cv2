/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.darwinmarketing.excel;

/**
 *
 * @author jackie
 */
public class EASheet {
    public String name = "";
    public EACell[][] cells = null;
    public EASheet(String sname){
        this.name = sname;
    }
}
