import { ClientNode } from './client.model';

export interface Deployment {

    id: number;

    name: string;

    containerName?: string;
    configuration: string;

    targetNodes: ClientNode[];

}

export type DeploymentState = 'WaitingToBePickedUp' | 'Pending' | 'Successful' | 'Error';

export interface DeploymentExecution {
    id: number;
    deployment: Deployment;
    target: ClientNode;
    state: DeploymentState;
    errorDescription?: string;

}