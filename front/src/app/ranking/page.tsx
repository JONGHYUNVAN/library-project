'use client'
import React, { useEffect, useRef ,useState} from "react";
import axios from 'axios';
import Link from 'next/link';
import Image from 'next/image'
import {Book} from '../interface/Book'
import {BarLoader, BounceLoader } from 'react-spinners';

export default function List() {
    const imagesRef = useRef<HTMLImageElement[]>([]);
    const width = 15;
    const height = 20;
    const [books, setBooks] = useState<Book[]>([]);
    const [loading, setLoading] = useState(true);


    useEffect(() => {
        axios.get(`${process.env.NEXT_PUBLIC_API_URL}/books`)
            .then((response) => {
                const dataArray = response.data.data;
                const books = dataArray.map((item: { id: string; title: string; imageURL: string}) => ({
                    id: item.id,
                    title: item.title,
                    imageURL: item.imageURL,
                }));
                setBooks(books);
                setLoading(false);
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
            <div className="container" style={{marginTop: '10vh', marginBottom: '5vh'}}>
                <div className="smallTitle">Ranked In TOP 10</div>
            </div>

            <div className="book-container">
                {books.slice(0, 5).map((book, index) => (
                    <div className="book" key={index}>
                        <Link href={`/search?id=${book.id}`} style={{ textDecoration: 'none'}} >
                            {loading ?
                                <div className="book-image">
                                    <BounceLoader color="#8B4513" size={20*width} />
                                </div>
                                                 :
                                <img className="book-image"
                                     src={book.imageURL}
                                     ref={el => imagesRef.current[index] = el as HTMLImageElement}
                                />
                            }
                         {loading ?
                             <div className="book-title">
                                <BarLoader color="#8B4513" width={20*width}/>
                             </div>
                                        :
                        <p className="book-title">{book.title}</p>
                         }
                        </Link>
                    </div>
                ))}
            </div>

            <div className="book-container">
                {books.slice(5, 10).map((book, index) => (
                    <div className="book" key={index}>
                        <Link href={`/search?id=${book.id}`} style={{ textDecoration: 'none'}} >
                            {loading ?
                                <div className="book-image">
                                    <BounceLoader color="#8B4513" size={20*width} />
                                </div>
                                :
                                <img className="book-image"
                                     src={book.imageURL}
                                     ref={el => imagesRef.current[index+5] = el as HTMLImageElement}
                                />
                            }
                            {loading ?
                                <div className="book-title">
                                    <BarLoader color="#8B4513" width={20*width}/>
                                </div>
                                :
                                <p className="book-title">{book.title}</p>
                            }
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