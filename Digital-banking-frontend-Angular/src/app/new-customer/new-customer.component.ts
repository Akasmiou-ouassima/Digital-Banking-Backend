import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Customer} from "../model/customer.model";
import {CustomerService} from "../services/customer.service";
import {throwError} from "rxjs";
import {Router} from "@angular/router";

@Component({
  selector: 'app-new-customer',
  templateUrl: './new-customer.component.html',
  styleUrls: ['./new-customer.component.css']
})
export class NewCustomerComponent implements OnInit {
  newCustomerFormGroup: FormGroup | undefined;
  errorMessage!: string;

  constructor(
    private fb: FormBuilder,
    private customerService: CustomerService,
    private router: Router) { }

  ngOnInit(): void {
    this.newCustomerFormGroup = this.fb.group({
      name : this.fb.control(null, [Validators.required, Validators.minLength(5)]),
      email : this.fb.control(null, [Validators.required, Validators.email])
    });
  }

  handleSaveNewCustomer() {
    let customer: Customer = this.newCustomerFormGroup?.value;
    this.customerService.saveNewCustomer(customer).subscribe({
      next: data => {
        alert('New Customer has been successfully saved!');
        this.newCustomerFormGroup?.reset();
        this.router.navigateByUrl("/customers").then(r => {});
      },
      error: err => {
        this.errorMessage = err.message;
        return throwError(err);
      }
    });
  }
}
