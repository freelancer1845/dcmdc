import { ClientNode } from './client.model';

export interface Deployment {

    id: number;

    name: string;

    containerName?: string;
    configuration: string;

    targetNodes: ClientNode[];

}

