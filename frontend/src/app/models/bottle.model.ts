export interface Bottle {
    id: number;
    volume: number;
    price: number;
    purchaseDate: string;
    wine: {
        id: number;
        name: string;
        vintage: number;
        color: 'RED' | 'WHITE' | 'ROSE' | 'SPARKLING' | 'YELLOW' | 'DESSERT';
        appellation?: string;
        producer: {
            id: number;
            name: string;
        };
        region: {
            id: number;
            name: string;
            country: {
                id: number;
                name: string;
            };
        };
    };
    rack?: {
        id: number;
        name: string;
    };
}
