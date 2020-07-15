getInfo();

function getInfo() {
    const wrapper = document.getElementById("wrapper");
    wrapper.innerHTML = `
        <h1>About Me</h1> 
        <p>
            <span id="hello-world-greeting">Hello World!</span> My name is Raluca Tudor, and I just finished my first year of studies 
            towards a BSc degree in Computer Science. I am also the Student Representative of the 
            Olympics' Class of 29 students, including myself. I am an enthusiastic, persevering and meticulous CS Student, 
            longing to acquire as much knowledge as possible.
        </p>

        <section id="education">
            <h1>Education</h1>
            <section>
                <header>
                    <h1>
                        Faculty of Mathematics and Computer Science,<br/>
                        University of Bucharest
                    </h1>
                    <span class="time-span">Oct. 2019 &ndash; June 2022</span>
                    <div>BSc in Computer Science</div>
                </header>
                <p>
                    ...
                </p>
            </section>
            <section>
                <header>
                    <h1>
                        "Tudor Vianu" National College of Computer Science,<br/> 
                        Bucharest
                    </h1>
                    <span class="time-span">Sept. 2015 &ndash; June 2019</span>
                    <div>High School</div>
                </header>
                <p>
                    ...
                </p>
            </section>
        </section>
        <section id="experience">
            <h1>Experience</h1>
            <section>
                <header>
                    <h1>Student Training in Engineering Program [STEP] Intern at Google</h1>
                    <span class="time-span">July 6 &ndash; Sept. 25, 2020</span>
                </header>
                <p>
                    ...
                </p>
            </section>
        </section>

        <h2>Skills</h2>

        <h2>Volunteering</h2>

        <h2>Hobbies</h2>
    `;
}