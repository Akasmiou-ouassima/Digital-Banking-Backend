import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../services/authentication.service";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginFormGroup! : FormGroup;
  constructor(private fb : FormBuilder, private authService:AuthenticationService, private router:Router) { }

  ngOnInit(): void {
    this.loginFormGroup=this.fb.group({
      username : this.fb.control(null, [Validators.required]),
      password : this.fb.control(null,[Validators.required])
    });
  }

  onLogin(dataForm: any) {
    this.authService.login(dataForm.username, dataForm.password);
    if (this.authService.isAuthenticated){
      this.router.navigateByUrl('');
    }
  }
}
