export interface BookImage {
    id: number;
    title: string;
    imageURL: string;
}

export interface Post {
    id: number;
    title: string;
    content: string;
    createdAt: string;
    updatedAt: string;
    authorNickName: string;
    bookImage: BookImage;
}
