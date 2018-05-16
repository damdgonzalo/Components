using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class PlayerController : MonoBehaviour {

    Animator anim;
    Rigidbody2D rb;

    public float speedX;
    public float jumpSpeedY;

    bool facingRight;
    bool jumping;
    bool doubleJump;
    float speed;

    public GameObject leftBullet, rightBullet;

    Transform firePos;

    public Text text;

	// Use this for initialization
	void Start () {
        anim = GetComponent<Animator>();
        rb = GetComponent<Rigidbody2D>();

        firePos = transform.Find("FirePos");
	}
	
	// Update is called once per frame
	void Update () {
        MovePlayer(speed);
        Flip();

        if (Input.GetKeyDown(KeyCode.J)) {
            speed = -speedX;
        }

        if (Input.GetKeyUp(KeyCode.J)) {
            speed = 0;
        }

        if (Input.GetKeyDown(KeyCode.L)) {
            speed = speedX;
        }
        if (Input.GetKeyUp(KeyCode.L)) {
            speed = 0;
        }

        if (Input.GetKeyDown(KeyCode.I)) {

            if (jumping)
            {
                if (!doubleJump)
                {
                    rb.AddForce(new Vector2(rb.velocity.x, jumpSpeedY));
                    anim.SetInteger("State", 2);
                    doubleJump = true;
                }
            }
            else
            {
                jumping = true;
                doubleJump = false;
                rb.AddForce(new Vector2(rb.velocity.x, jumpSpeedY));
                anim.SetInteger("State", 2);
            }
            
        }

        if (Input.GetKeyDown(KeyCode.Space)) {
            Fire();
        }
    }
    

    public void Fire() {
        if (facingRight) Instantiate(leftBullet, firePos.position, Quaternion.identity);
        else Instantiate(rightBullet, firePos.position, Quaternion.identity);
    }

    void MovePlayer(float playerSpeed) {
        if (playerSpeed < 0 && !jumping || playerSpeed>0 && !jumping) {
            anim.SetInteger("State", 1);
        }

        rb.velocity = new Vector3(speed, rb.velocity.y, 0);

        if (playerSpeed==0 && !jumping) {
            anim.SetInteger("State", 0);
        }
    }


    void OnCollisionEnter2D(Collision2D other) {
        
        if (other.gameObject.tag == "Ground") {
            jumping = false;
            anim.SetInteger("State", 0);
        }

        else if (other.gameObject.tag == "Enemy")
        {

            text.text = "AYLMAO";
           anim.SetInteger("State", 3);
        }
    }


    void Flip() {
        if (speed<0 && !facingRight || speed>0 && facingRight) {
            facingRight = !facingRight;

            Vector3 temp = transform.localScale;

            temp.x = -temp.x; 
            transform.localScale = temp;
        }
    }

    public void WalkLeft() {
        speed = -speedX;
    }

    public void WalkRight() {
        speed = speedX;
    }

    public void Jump() {
        jumping = true;

        rb.AddForce(new Vector2(rb.velocity.x, jumpSpeedY));

        anim.SetInteger("State", 2);
    }

    public void Stop() {
        speed = 0;
    }
}
