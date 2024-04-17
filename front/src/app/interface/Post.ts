export interface Post {
    id: number;
    title: string;
    content: string;
    createdAt: string;
    updatedAt: string;
    views: number;
    authorNickName: string;
    profile: number;
    bookImage: BookImage;
    isAuthor: boolean;
}

export interface BookImage {
    id: number;
    title: string;
    imageURL: string;
}

export interface Comment{
    id: number;
    content: string;
    authorNickName: string;
    createdAt: string;
    updatedAt: string;
}