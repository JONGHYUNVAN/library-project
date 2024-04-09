import Link from 'next/link';


export default function Home() {
  let name = 'J.H.VAN';
  return (
      <>
          <head>
              <title> Welcome! </title>
          </head>

          <div className="main-page">
              <div className="title"
                   style={{position: 'relative', backgroundSize: 'cover', backgroundPosition: 'center'}}>
                  <h4 className="titleLetter">L</h4>
                  <h4 className="titleLetter">i</h4>
                  <h4 className="titleLetter">b</h4>
                  <h4 className="titleLetter">r</h4>
                  <h4 className="titleLetter">a</h4>
                  <h4 className="titleLetter">r</h4>
                  <h4 className="titleLetter">y</h4>
                  <span className="title-space"></span>
                  <h4 className="titleLetter">p</h4>
                  <h4 className="titleLetter">r</h4>
                  <h4 className="titleLetter">o</h4>
                  <h4 className="titleLetter">j</h4>
                  <h4 className="titleLetter">e</h4>
                  <h4 className="titleLetter">c</h4>
                  <h4 className="titleLetter">t</h4>
                  <span className="title-sub">by {name}</span>
              </div>
              <Link href="/ranking">
              <video poster={"/backposter.png"} className="background-video" autoPlay muted loop >
                  <source src="/background.mp4" type="video/mp4" />
                </video>
              </Link>
          </div>

      </>
  );
}
