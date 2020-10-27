import { MediaMatcher } from '@angular/cdk/layout';
import { ChangeDetectorRef, Component, Input, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NewClientDialogComponent } from '@components/dialgos/new-client-dialog/new-client-dialog.component';
import { NewDeploymentDialogComponent } from '@components/dialgos/new-deployment-dialog/new-deployment-dialog.component';

export interface NavLink {
  label: string,
  route: string,
}

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnDestroy {

  @Input() links: NavLink[];

  mobileQuery: MediaQueryList;

  private _mobileQueryListener: (event: any) => void;

  constructor(changeDetectorRef: ChangeDetectorRef, media: MediaMatcher, private dialog: MatDialog) {
    this.mobileQuery = media.matchMedia('(max-width: 600px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addEventListener('change', this._mobileQueryListener);
  }

  ngOnDestroy(): void {
    this.mobileQuery.removeEventListener('change', this._mobileQueryListener);
  }

  newClient() {
    this.dialog.open(NewClientDialogComponent).afterClosed().subscribe();
  }

  newDeployment() {
    this.dialog.open(NewDeploymentDialogComponent).afterClosed().subscribe();
  }
}
