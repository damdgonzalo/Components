using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class DestroyWithDelay : MonoBehaviour
{

    public float delay;

    // Use this for initialization
    void Start()
    {
        Destroy(gameObject, delay);
    }

    void OnCollisionEnter2D(Collision2D other)
    {

        if (other.gameObject.tag == "Enemy")
        {
            Destroy(gameObject);
        }
    }
}
