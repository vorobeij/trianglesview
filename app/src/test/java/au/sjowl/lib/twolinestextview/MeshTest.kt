package au.sjowl.lib.twolinestextview

import au.sjowl.libs.mesh.Mesh
import org.junit.Test

class MeshTest {

    @Test
    fun createMeshTest() {
        val w = 1080
        val h = 1920
        val dots = 40
        val mesh = Mesh(w, h, dots)
        mesh.createMesh()
        println(mesh)

        assert(mesh.mesh.filter { it.a.x > w }
            .filter { it.b.x > w }
            .filter { it.c.x > w }
            .isEmpty()
        )
        assert(mesh.mesh.filter { it.a.y > h }
            .filter { it.b.y > h }
            .filter { it.c.y > h }
            .isEmpty()
        )
    }

    @Test
    fun colorTest() {
    }
}
