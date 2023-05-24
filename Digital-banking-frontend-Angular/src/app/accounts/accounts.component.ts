import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {AccountsService} from "../services/accounts.service";
import {catchError, Observable, throwError} from "rxjs";
import {AccountDetails} from "../model/account.model";

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.css']
})
export class AccountsComponent implements OnInit {
  accountFormGroup!: FormGroup;
  currentPage: number = 0;
  pageSize : number = 5;
  accountOperations$!: Observable<AccountDetails>;
  operationFormGroup!: FormGroup;
  errorMessage!: Object;
  errorAccountMessage!: Object;

  constructor(private fb: FormBuilder,
              private accountService: AccountsService) { }

  ngOnInit(): void {
    this.accountFormGroup = this.fb.group({
      accountId : this.fb.control('') // ['']
    });
    this.operationFormGroup = this.fb.group({
      //accountId : this.fb.control(this.accountFormGroup.value),
      operationType : this.fb.control(null),
      amount : this.fb.control(0),
      description : this.fb.control(null),
      accountTarget: this.fb.control(null)
    })
  }

  handleSearchAccountOperations() {
    let accountId: string = this.accountFormGroup.value.accountId;

    this.accountOperations$ = this.accountService.getAccountOperations(accountId, this.currentPage, this.pageSize).pipe(
      catchError(err => {
        this.errorAccountMessage = err.message;
        return throwError(err);
      })
    );
  }


  goToPage(page: number) {
    this.currentPage = page;
    this.handleSearchAccountOperations();
  }

  handleAccountOperation() {
    let accountId : string = this.accountFormGroup.value.accountId;
    let amount : number = this.operationFormGroup.value.amount;
    let description : string = this.operationFormGroup.value.description;
    let accountTarget: string = this.operationFormGroup.value.accountTarget;
    let operationType = this.operationFormGroup.value.operationType;

    if(operationType == 'DEBIT') {
      this.accountService.debit(accountId, amount, description).subscribe({
        next: (data) => {
          alert("Success Debit");
          this.operationFormGroup.reset();
          this.handleSearchAccountOperations();
        },
        error : (err) => {
          this.errorMessage = err.message;
      }
      });
    } else if( operationType == 'CREDIT') {
      this.accountService.credit(accountId, amount, description).subscribe({
        next: data => {
          alert("Success Credit");
          this.operationFormGroup.reset();
          this.handleSearchAccountOperations();
        },
        error: err => {
          this.errorMessage = err.message;
        }
      });
    } else if( operationType == 'TRANSFER') {
      this.accountService.transfer(accountId, accountTarget, amount, description).subscribe({

        next: data => {
          alert("Transfer Success");
          this.operationFormGroup.reset();
          this.handleSearchAccountOperations();
        },
        error: err => {
          this.errorMessage = err.message;
        }
      });
    }
  }
}
