using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameController : MonoBehaviour {

    public GameObject enemy;

    Transform enemySpawn;
    public float spawnTime;
    float spawnTimeCopy;

    // Use this for initialization
    void Start () {
        enemySpawn = transform.Find("enemySpawn");
        spawnTimeCopy = spawnTime;
    }
	
	// Update is called once per frame
	void Update () {
        spawnTime -= Time.deltaTime;
        if (spawnTime <= 0)
        {
            Instantiate(enemy, enemySpawn.position, Quaternion.identity);
            spawnTime = spawnTimeCopy;
        }
    }
}