export interface ClientNode {
    apiId: string;
    apiSecret: string;
    name: string;
}

export interface ClientStatus {
    uuid: string;
    cpuUsage: string;
    memoryUsage: string;
    maxMemory: string;
    timestamp: number;
}