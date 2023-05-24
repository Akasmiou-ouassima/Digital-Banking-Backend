import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CustomersComponent} from "./customers/customers.component";
import {AccountsComponent} from "./accounts/accounts.component";
import {NewCustomerComponent} from "./new-customer/new-customer.component";
import {UpdateCustomerComponent} from "./update-customer/update-customer.component";
import {CustomerAccountsComponent} from "./customer-accounts/customer-accounts.component";
import {CustomerComponent} from "./customer/customer.component";
import {BankAccountsComponent} from "./bank-accounts/bank-accounts.component";
import {NewAccountComponent} from "./new-account/new-account.component";

const routes: Routes = [
  { path: "customers", component: CustomersComponent },
  { path: "accounts", component: AccountsComponent },
  { path: "new-customer", component: NewCustomerComponent },
  { path: "update-customer/:id", component: UpdateCustomerComponent },
  { path: "customer-accounts/:id", component: CustomerAccountsComponent },
  { path: "customers/:id", component: CustomerComponent },
  { path: "bank-accounts", component: BankAccountsComponent },
  { path: "customers/new-account/:id", component: NewAccountComponent }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  declarations: [
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
