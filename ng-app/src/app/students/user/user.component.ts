import {Component, OnInit} from '@angular/core';

import {User} from "../user";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  user:any={};
  roles:Array<string>=['Admin','User'];
  public refreshValue(value:any):void{
    this.user.role=value.text;
  }

  constructor() {
  };

  ngOnInit() {
    this.user= new User();
  }






}
