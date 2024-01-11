'use client'
import React, { useEffect, useRef ,useState} from "react";
import axios from 'axios';
import Link from 'next/link';

export default function List() {
    const imagesRef = useRef<HTMLImageElement[]>([]);
    const width = 15;
    const height = 20;

    interface Book {
        id: string;
        title: string;
        imageURL: string;
        publisher: string;
        author : string;
        status : string;
        libraryName : string;
    }
    const [books, setBooks] = useState<Book[]>([]);

    useEffect(() => {
        axios.get('http://localhost:8080/books/')
            .then((response) => {
                const dataArray = response.data.data;
                const books = dataArray.map((item: { id: string; title: string; imageURL: string, publisher: string, author : string, status : string, libraryName : string}) => ({
                    id: item.id,
                    title: item.title,
                    imageURL: item.imageURL,
                    publisher:  item.publisher,
                    author: item.author,
                    status: item.status,
                    libraryName: item.libraryName
                }));
                setBooks(books);
            })
            .catch((error) => {
                console.error(error);
            });
    }, []);

    useEffect(() => {
        const handleMouseMove = (e: MouseEvent) => {
            const x = e.offsetX;
            const y = e.offsetY;

            imagesRef.current.forEach((image) => {
                if (image === e.target) {
                    const rect = image.getBoundingClientRect();
                    const centerX = rect.width / 2
                    const centerY = rect.height / 2;

                    const distanceX = centerX-x;
                    const distanceY = centerY-y;

                    const maxDistance = Math.sqrt(centerX * centerX + centerY * centerY);

                    const rotateY =  Math.abs(distanceY / maxDistance) * 45;
                    const rotateX =  Math.abs(distanceX / maxDistance) * 45;

                    if (Math.abs(distanceX) <= rect.width / 2  &&
                        Math.abs(distanceY) <= rect.height / 2){
                        image.style.width = `${width*1.5}vw`;
                        image.style.height = `${height * 1.5}vw`;
                        if(distanceY>0) {
                            if(distanceX>0)
                                image.style.transform = `perspective(20vw) rotateX(${-rotateY}deg) rotateY(${rotateX}deg)`;
                            else
                                image.style.transform = `perspective(20vw) rotateX(${-rotateY}deg) rotateY(${-rotateX}deg)`;

                        }
                        else  {
                            if(distanceX>0)
                                image.style.transform = `perspective(20vw) rotateX(${rotateY}deg) rotateY(${rotateX}deg)`;
                            else
                                image.style.transform = `perspective(20vw) rotateX(${rotateY}deg) rotateY(${-rotateX}deg)`;
                        }
                    }

                }
                else {
                    image.style.width = `${width}vw`;
                    image.style.height = `${height}vw`;
                    image.style.transform = 'rotateX(0deg) rotateY(0deg)';

                }
            });
        };
        const handleMouseLeave = (e: MouseEvent) => {
            const image = e.target as HTMLImageElement;
            image.style.width = `${width}vw`;
            image.style.height = `${height}vw`;
            image.style.transform = 'rotateX(0deg) rotateY(0deg)';
        };

        const images = Array.from(document.querySelectorAll(".book-image")) as HTMLImageElement[];
        imagesRef.current = images;

        images.forEach((image) => {
            image.style.transformStyle = "preserve-3d";
            image.style.transform = "rotateX(0deg) rotateY(0deg)";
            image.addEventListener("mousemove", handleMouseMove);
        });

        return () => {
            images.forEach((image) => {
                if (image) {
                    image.removeEventListener("mousemove", handleMouseMove);
                    image.addEventListener("mouseleave", handleMouseLeave);
                }
            });
        };
    }, [books]);




    return (
        <div>
            <div className="smallTitle">
                <h4>Ranked In TOP 10</h4>
            </div>

            <div className="book-container">
                {books.slice(0, 5).map((book, index) => (
                    <div className="book" key={index}>
                        <Link href={`/search?id=${book.id}`} style={{ textDecoration: 'none'}} >
                            <img className="book-image"
                                 src={book.imageURL}
                                 alt="book cover"
                                 ref={el => imagesRef.current[index] = el as HTMLImageElement}
                            />
                        <p className="book-title">{book.title}</p>
                        </Link>
                    </div>
                ))}
            </div>

            <div className="book-container">
                {books.slice(5, 10).map((book, index) => (
                    <div className="book" key={index}>
                        <Link href={`/search?id=${book.id}`} style={{ textDecoration: 'none'}} >
                            <img className="book-image" src={book.imageURL}
                                 alt="book cover"
                                 ref={el => imagesRef.current[index + 5] = el as HTMLImageElement}
                            />
                            <p className="book-title">{book.title}</p>
                        </Link>
                    </div>
                ))}
            </div>

            <video className="background-video" autoPlay muted loop>
                <source src="/backgroundList.mp4" type="video/mp4" />
            </video>
        </div>
    );
}