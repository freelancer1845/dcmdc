export interface ClientNode {
    uuid: string;
    name: string;
    description: string;
}

export interface ClientStatus {
    uuid: string;
    cpuUsage: string;
    memoryUsage: string;
    maxMemory: string;
    timestamp: number;
}