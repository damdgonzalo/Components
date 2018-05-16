using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class EnemyController : MonoBehaviour
{

    Animator anim;
    Rigidbody2D rb;

    public float speedX;

    bool facingRight;
    float speed;

    float attackDelay;
    float walkDelay;

    // Use this for initialization
    void Start()
    {
        anim = GetComponent<Animator>();
        rb = GetComponent<Rigidbody2D>();

        attackDelay = 1;
        walkDelay = Random.Range(1,5);
        anim.SetInteger("State", 1);
        speed = -speedX;
    }

    // Update is called once per frame
    void Update()
    {
        MovePlayer(speed);
        Attack();
    }


    void MovePlayer(float playerSpeed)
    {
        anim.SetInteger("State", 1);
        rb.velocity = new Vector3(speed, rb.velocity.y, 2);
        walkDelay -= Time.deltaTime;

        if (walkDelay <= 0) {
            
            if (speed > 0) speed = -speedX;
            else speed = speedX;
            
            Flip();
            walkDelay = Random.Range(1, 5); ;
        }
   
    }

    void Attack()
    {
        attackDelay -= Time.deltaTime;
        if (attackDelay <= 0)
        {
            anim.SetInteger("State", 2);
            attackDelay = 1;
        }

    }


    void Flip()
    {
        //if (speed < 0 && !facingRight || speed > 0 && facingRight)
        //{
            facingRight = !facingRight;

            Vector3 temp = transform.localScale;

            temp.x = -temp.x;
            transform.localScale = temp;
        //}
    }

    void OnCollisionEnter2D(Collision2D other) {

        if (other.gameObject.tag == "Bullet")
        {
            anim.SetInteger("State", 3);
        }
    }

        public void WalkLeft()
    {
        speed = -speedX;
    }

    public void WalkRight()
    {
        speed = speedX;
    }

    public void Stop()
    {
        speed = 0;
    }
}
