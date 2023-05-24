import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { CustomersComponent } from './customers/customers.component';
import { AccountsComponent } from './accounts/accounts.component';
import {HttpClientModule} from "@angular/common/http";
import {ReactiveFormsModule} from "@angular/forms";
import { NewCustomerComponent } from './new-customer/new-customer.component';
import { UpdateCustomerComponent } from './update-customer/update-customer.component';
import { CustomerAccountsComponent } from './customer-accounts/customer-accounts.component';
import { CustomerComponent } from './customer/customer.component';
import { BankAccountsComponent } from './bank-accounts/bank-accounts.component';
import { NewAccountComponent } from './new-account/new-account.component';
import {AlertComponent} from "./alert/alert.component";
import { LoginComponent } from './login/login.component';


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    CustomersComponent,
    AccountsComponent,
    NewCustomerComponent,
    UpdateCustomerComponent,
    CustomerAccountsComponent,
    CustomerComponent,
    BankAccountsComponent,
    NewAccountComponent,
    AlertComponent,
    LoginComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [],
  exports: [
    AlertComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
