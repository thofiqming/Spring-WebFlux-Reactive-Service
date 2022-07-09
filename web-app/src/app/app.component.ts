import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'web-app';
  values: any = [];
   list: any = [];

  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
    console.log('init application method invoked');
  }

  findShipmentsData(): void {
    this.findShipments().subscribe(event => {
      console.log(event);
      this.list.push(event);
      console.log(this.list.length);
    });
  }

  findShipments(): Observable<any> {
    return Observable.create((observer) => {
      const eventSource = new EventSource(`http://localhost:8080/shipment/v1/values`);
      eventSource.onmessage = (event) => {
        console.log('eventSource.onmessage: ', event);
        observer.next(event.data);
      };
      eventSource.onerror = (error) => observer.error('eventSource.onerror: ' + error);
      return () => eventSource.close();
    });
  }

  submit(): void {
    console.log('calling submit');
    /*this.http.get('http://localhost:8080/shipment/v1/test', {responseType: 'text'})
      .subscribe(value => {
        console.log('data printing');
        console.log(value);
      });*/

    this.http.get('http://localhost:8080/shipment/v1/values', {responseType: 'text'})
      .subscribe(value => {
        console.log('data printing');
        console.log(value);
        this.values.push(value);
      });
  }

}
