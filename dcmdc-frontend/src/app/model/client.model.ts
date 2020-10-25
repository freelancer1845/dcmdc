export interface ClientNode {
    api_id: string;
    api_secret: string;
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