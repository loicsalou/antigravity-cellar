export interface User {
    id: number;
    email: string;
    name: string;
}

export interface Cellar {
    id: number;
    name: string;
    racks?: Rack[];
}

export interface Rack {
    id: number;
    name: string;
    type: string;
    photoUrl?: string;
    bottles?: Bottle[];
}

export interface Producer {
    id: number;
    name: string;
    description?: string;
    website?: string;
    email?: string;
    phone?: string;
}

export interface Region {
    id: number;
    name: string;
    country?: Country;
}

export interface Country {
    id: number;
    name: string;
    code?: string;
}

export interface Wine {
    id: number;
    name: string;
    vintage?: number;
    appellation?: string;
    color?: 'RED' | 'WHITE' | 'ROSE' | 'SPARKLING' | 'DESSERT' | 'YELLOW';
    producer?: Producer;
    region?: Region;
}

export interface Bottle {
    id: number;
    wine: Wine;
    rack?: Rack;
    positionX?: number;
    positionY?: number;
    price?: number;
    purchaseDate?: string;
}
