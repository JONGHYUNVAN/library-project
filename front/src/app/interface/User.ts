export interface UpdateUser{
    name?: string | null;
    nickName?: string | null;
    phoneNumber?: string | null;
    gender?: 'MALE' | 'FEMALE';
}